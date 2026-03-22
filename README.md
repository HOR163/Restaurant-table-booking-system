# Restaurant table booking system

## Used technologies
* Java 25 LTS
* Springboot 4.0.3
* PostgreSQL 18
* React 19.2.4
* Node 24.13.0 LTS


## Setup
This guide assumes you use Intellij for java developement.

### Backend

#### Database
1. Go to /backend folder
2. Set password and username for the database (in `docker-compose.yml`)
3. Start the database with `docker compose up` (if you already don't have it, then install docker or docker desktop)
4. To stop the database, use `docker compose down` command
**NOTE:** NEVER PUSH THE USERNAME AND PASSWORD, remove them after the container has been set up  

#### Spring
1. Go to `backend/src/main/resources`
2. Make a copy of `application.properties` and name it as `application-local.properties`
3. Fill in the missing values for configuration
4. In the run configuration (top right, press the three-dot button and then `edit`), set `active profiles` to `local`
5. Before running let gradle load all the dependencies

To run it purely in console, then go to `/backend` and build the project `gradle build`, after that go to `/backend/libs/` and run the created jar file with `java -jar [FILE]`

#### Extras
You can setup the database view inside the editor, for that:
1. click on the database icon on the left side of the screen
2. In the sidebar, click on the `+` symbol
3. Select `data source` and then `PostgreSQL`
4. In the popup window, leave everything as-is, but do change the `user` and `password` to those defined in `docker-compose.yml`
5. Test the connection, if it is ok, then hit `OK`

### Frontend
1. Install Node.js
2. Go to /frontend folder
3. Run `npm install`
4. Run the frontend with `npm start`
5. If you want, you can build it with `npm build`

NOTE: To avoid CORS, then the frontend uses a proxy in `package.json` using `http://127.0.0.1:8080`. Thus the backend should run on port 8080.

### Sample data
If you want to enable sample data, then go to file `backend/src/main/resources/db/changelog/db.changelog-master.yml` and uncommment the last two lines.  
This will add changesets that will add some AI generated data.

## Analysis
The analysis is put in the `/docs/analysis.md` file  
AI usage is in `/docs/ai_usage.md` file

## Time management
- Setup (base projects) - 2h
- Database planning, setup and creation - 3h 30m
- Entities, DTOs, repositories - 1h
- Mappers and templates for controllers - 2h 30m
- Service templates + planning functions - 1h
- Basic service implementation and integration with controllers - 3h
- Setting up and using custom ApplicationExceptions - 1h
- Timeslot finding - 2h 30m
- Defining abstract BaseApiService, creating restaurant list view - 3h
- Updating BaseApiService, other services in frontend, DTOs - 4h
- Check for colliding booking times - 20m
- Add table number - 30m
- New booking: add state manager and attribute list - 3h 30m
- New booking: filters, score and booking creation - 4h
- My bookings view and sample data - 2h
- Analysis and some minor documentation - 1h
