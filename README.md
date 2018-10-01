# Baseline of Learner Profiles for students joining class (and unspecified cases in IL)

This component is responsible for creating baseline of learner profile for learners at different events.

### Baseline Specifications
For any student in a class there is a need to create the baseline. From system perspective, baseline should happen automatically without manual intervention. 

There are few events in which case baseline will be triggered:

- Students joining class (when course is already assigned to class)
- When a course is assigned to class (and class already has students)
- When teacher sets up the floor line for the class (and class already has students)


### Technical Scope

- Single API will be exposed which will be internal API. No authentication needed
- Baseline needs to happen once for combination of following tuples
	- Course
	- User
	- Class (may be null, in case of IL)

 
### Build and Run

To create the shadow (fat) jar:

    ./gradlew

To run the binary which would be fat jar from the project base directory:

    java -jar profile-baseline.jar $(project_loc)/src/main/resources/profile-baseline.json

### Baseline API
- This API will be called internally only
- It will return 200 response directly and will delegate processing to worker threads on message bus
- The API payload would contain source, class id and member ids array (optional, present only in case where source is class join)
- The API will be responsible for queue-ing the operation
- Now generate the data for messages based on source
    - class join, one event per member specified in payload will be created.
    - course assign to class, class members will be looked up and one event per member of class will be created
    - OOB, this won't be used by API per se, but by READ handler to post message along with member id in case of 404
        - This may also be used as API to trigger rescope on adhoc basis
- Note that here we won't validate if class member may have (with current UI flows may not be possible) baselined profile already. That will be done downstream
- Downstream processor will be responsible to process the data
- For processing the better of learner profile and class floor setting need to be considered
- While processing, the DB persistence will be only with skyline and not inferred competencies
- After the data is processed, the output of baseline learner profile API will be created and cached as a JSON packet in DB

### Design

#### Problem
- Have a batch processing kind of infra
- Should be able to do it in parallel
- Queueing should be backed by persistence
- Queueing should be open so that others can also request for queueing their stuff
- Avoid concurrent processing of same record

#### Solution
- Have a batch processing kind of infra using parallelization would be achieved using worker threads working over message bus
- For persistence DB will be used
- Event bus will provide a mechanism for open ended trigger end point
- Do redundant checks and store state to avoid multiple processing of same record

#### Facets of Design
- Core processing
    - Should be triggered with receiving some key to identify as to what needs processing
- Controller processing
    - Event bus to receive two kinds of messages
        - Queue
        - Process
    - Both of these events will be fire and forget
    - For Queue messages, entry will be made into DB table
    - Now some mechanism needs to be in place to to read the DB table and generate Process messages
    - Need a timer thread in place
    - The queue in DB is going to have a status field with values - null, dispatched, processing
    - Record will be inserted in queue with status as null
    - When timer thread picks that up and sends to message bus, it will be marked as dispatched
    - When worker threads pick up the record to process, they first check to see if the record is present in table with status as dispatched and record is not present in rescope table, then the record will be marked as processing and will be processed
    - Once processing is done, this record will be deleted from queue
    - For the first run of timer thread, it should clean up all statuses in DB queue so that they are picked up for processing downstream
    - The number of records that are read from DB/queue and dumped on to message bus for processing, needs to be configurable

