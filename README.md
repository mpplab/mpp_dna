# MPP_DNA
An Anonymous Data Publishing Framework for the Streaming Genomic Data

Summary
----------------------
This program was written by Yaqing Mao to implement Wu Xiang's anonymous data publishing framework, as described in his paper "An Anonymous Data Publishing Framework for the Streaming Genomic Data", XuZhou Medical University, 2016. 
The material includes simulation program code and datasets used in paper. 
The code is in the 'GeneDataStreamingPublish' folder and the data is in the 'MPP_DNA' folder.

Build and test code
----------------------
**0. Requirements**: 
The programe is based on Java 7 and we use Eclipse as integrated development environment.
All you need to build it is **JDK 7** and **Eclipse**.

**1. Prepare the data**
In order to test the algorithms in the program, data should be prepared in advance.
All you need to do is download the 'MPP_DNA' folder in your computer and set the path in the code.
How to set the path will be introduced latter.

**2. Import the project in Eclipse**
The 'GeneDataStreamingPublish' folder downloaded should be moved to your workspase of Eclipse.
Then, import the existing project 'GeneDataStreamingPublish' in the workspace.

**3. Project settings**
The parameters need setting are all listed in 'public_data.java'.

Dependency | Version
------------ | -------------
MPP_DataPfx | Data store path prefix. (The path the 'MPP_DNA' downloaded in)
DataSource | 'Sequence', 'Sequence2', 'Sequence3' is supported in the project
DelayCount | Set the delay constraint in NSPSGD and WSPSGD
SprsHold | Set the suppression threshold in WSPSGD
logName | Set the log file name
AlName | Set the algorithm name
Debug | Whether output the debug info to the console
StaticCount | Set the static handling count in NSPSGD, WSPSGD and Hybrid
OutArgIndex | Set sequence number of output average distance

**4. Run the algorithms**
The projects simulate 4 algorithms: NSPSGD, WSPSGD, Hybrid and DNALA, corresponding to 'AL1_NSPSGD.java', 'AL2_WSPSGD.java', 'AL3_Hybrid.java' and 'AL4_DNALA.java'.
You can run the class to test the corresponding algorithm.
