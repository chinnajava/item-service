
# item-service

#### how to run:
        checkout the project and run the following command
        mvn spring-boot:run

    
#### Post data to service
        curl -i -X POST \
        http://localhost:8080/items \
         -H 'content-type: application/json' \
         -d '{"item": {
             "id": 2,
             "timestamp": "2017-10-16T22:46:32.189Z"
        }}'


### Get data from Service
        curl -X GET \
        http://localhost:8080/items
