# Analysis of the work

## Time manamgent
Time management was not that good: I should have concentrated more on the main part (booking), rather than the whole system with users, restaurants and so on. I would say it ate a bit of time out of the whole process, but the main thing was just starting early enough. Although, I started with the planning quite early, I should have gone more in depth then just a single png of the database design. 

## Features that were not implemented
### Mandatory features
#### Table view
As you might have already seen, the tables don't really make a grid :). To be honest I just wanted to get the booking part working and then deal with the visual part, however I kind of ran out of time. To be honest it shouldn't really be that hard: just add some fields to the database and draw the table on the screen with position relative for parent and position absolute for the table element. Then used the top and left properties to set the location by percentage (of the whole restaurant size, saved with the restaurant in the database).  

#### Scoring and "attributes"
The solution that came to mind with attributes was the one that I implemented. Instead of the table having n or more columns, the table has a list of attributes which the user can choose. This isn't the best solution as some fields might be contradicitng (ie user selects "outside" and "indoors" at the same time). However, this has the added beenfit of scalability: you can add as many attirbutes as you want. One thing that could be added are weights: for example the location could be more important, thus having a greater weight. With the scoring, it isn't the greatest, I tried to weight the scores so that it would both prioritize the number of people and the selected attributes, however it didn't turn out that great.

#### The "U" in CRUD
The current endpoints allow creating, getting and deleting objects, however you can't update them.

### Nice to have features
#### Accounts
One of the things I wanted to add, was accounts, so that you could have multiple users, doesn't really add to the main point of the exercise, but for some reason at first I though it would be important.

#### Swagger
Swagger would have been quite nice to have for the endpoints and it has good integration with spring as well.

#### Bean validation
To validate the frontend input better.

#### Score calculation in the backend
It would be a bit better if backend handled most of the score calculation, as there it is a bit simpler to do it in there (you have all the data in there). 

## Bugs / oversights
### Time, timezones and daylight savings
As Intellij chose OffsetDateTime for timezonetz type, then I went with that time and date class. Overall the time and date seem to work fine currently, when the server and client are in the same timezone, however it might not be true always, thus later a bit more attention must be placed on how the timezoens and time is handled between frontend and backend.

### More time
The backend should send the time slots with maximum minute preecision, however currently it likes to drift just a bit, so that it also returnes some milliseconds. It isn't anything breaking, but could be a bit nicer.

## Documentation
For the documentaition I though I was first going to write the code and then the documentation, and I didn't really get that far with the documentation. In reality, documentation should always be created alongside with the functions. I did in the end add some docstrings to bigger and more importand functions but the overall design and design choices are missing.

## Justification
There is non, should have done a better job and had a better focus on the task.

