# The Dining Philosophers Problem

## About
This project is the solution to the [Dining Philosophers](https://en.wikipedia.org/wiki/Dining_philosophers_problem) problem. It was assigned as a laboratory assignment for a course (COE628 - Operating Systems) I am currently taking at Ryerson University. 

![Dining Philosophers](https://en.wikipedia.org/wiki/Dining_philosophers_problem#/media/File:An_illustration_of_the_dining_philosophers_problem.png "Dining Philosophers")

By Benjamin D. Esham / Wikimedia Commons, CC BY-SA 3.0, https://commons.wikimedia.org/w/index.php?curid=56559


## The Problem
Five silent philosophers sit at a round table for dinner. Each philosopher has a plate of food and a fork on each adjacent side. These philosophers share the five forks available to them. Furthermore, they can only dine using both forks concurrently. This limits the diners from all dining simultaneously. There are various solutions to this problem, but all must overcome the challenges of concurrent algorithm design.   

## The Solution
This problem was to be solved through the use of semaphores in Java. However, the catch was that we could not use Java's built-in semaphore library and had to create our own. In my case, I used the semaphore class I created in a previous lab assignment. Enticed by the problem, once I had completed my solution I realized that although my solution incorporated the use of semaphores, it ran perfectly fine without it. Thus, I decided to split my solution into two solutions: the first of which was dependent on my sempahore class and can be found under the [Semaphore](https://github.com/demharusnam/DiningPhilosophers/tree/master/src/Semaphore) package of this project. The second, used the leftover code from the original solution to create one based entirely on the default Java library without the use of semaphores. This solution can be found under the [Default](https://github.com/demharusnam/DiningPhilosophers/tree/master/src/Default) package of this project.

This project was compiled on [Netbeans 8.2](https://netbeans.org/downloads/old/8.2/) as that is the software used for this course's assignments.


