
Run all from the `backend` folder!

# Backend Manual Tests Using CURL

## Setup

Install [JQ](https://stedolan.github.io/jq/)

```bash
sudo apt install jq
```

Launch backends

```bash
make copy-data
make serve-storage
make serve-analysis
make serve-runtime-mock
```

## List Files

```bash
curl http://localhost:8080/files/list | jq '.'
```

```json
[
  {
    "path": "README.md",
    "type": "FILE",
    "depth": 0,
    "length": 815,
    "lastModified": 1568184229192
  },
  {
    "path": "kraken.svg",
    "type": "FILE",
    "depth": 0,
    "length": 3383,
    "lastModified": 1568184229196
  }
]
```

## Create Test Result

```bash
curl -d '{"id":"resultId", "startDate":42, "endDate":42, "status":"STARTING", "description": "description", "type": "DEBUG"}' -H "Content-Type: application/json" -X POST http://localhost:8081/result | jq '.'
```

```json
{
  "path": "gatling/results/resultId/result.json",
  "type": "FILE",
  "depth": 3,
  "length": 108,
  "lastModified": 1568186290386
}
```

### Get Result JSON

```bash
curl http://localhost:8080/files/get/json?path=gatling/results/resultId/result.json | jq '.'
```

```json
{
  "id": "resultId",
  "startDate": 42,
  "endDate": 42,
  "status": "STARTING",
  "description": "description",
  "type": "DEBUG"
}
```

## Set Result Status

```bash
curl -X POST http://localhost:8081/result/status/RUNNING?resultId=resultId | jq '.'
```

```json
{
  "path": "gatling/results/resultId/result.json",
  "type": "FILE",
  "depth": 3,
  "length": 106,
  "lastModified": 1568187054353
}

```

## Add Debug Entry

```bash
curl -d @integration-tests/debug-entry.json -H "Content-Type: application/json" -X POST http://localhost:8081/result/debug | jq '.'
```

```json
{
  "id": "debugId",
  "resultId": "resultId",
  "date": 42,
  "requestName": "request_1",
  "requestStatus": "OK",
  "session": "session",
  "requestUrl": "GET http://kraken.octoperf.com/",
  "requestHeaders": [
    {
      "key": "Accept",
      "value": "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
    },
    {
      "key": "DNT",
      "value": "1"
    },
    {
      "key": "Accept-Language",
      "value": "en-US,en;q=0.5"
    }
  ],
  "requestCookies": [
    "Cookie"
  ],
  "requestBodyFile": "debugId-request.txt",
  "responseStatus": "303 See Other",
  "responseHeaders": [
    {
      "key": "Server",
      "value": "nginx/1.12.2"
    },
    {
      "key": "Date",
      "value": "Fri, 05 Apr 2019 11:52:04 GMT"
    },
    {
      "key": "Content-Length",
      "value": "0"
    }
  ],
  "responseBodyFile": "debugId-response.txt"
}
```

### List Debug Entries

```bash
curl "http://localhost:8080/files/find?rootPath=gatling/results/resultId/&matcher=debugId.*" | jq '.'
```

```json
[
  {
    "path": "gatling/results/resultId/debugId-response.txt",
    "type": "FILE",
    "depth": 3,
    "length": 25,
    "lastModified": 1568190870533
  },
  {
    "path": "gatling/results/resultId/debugId.debug",
    "type": "FILE",
    "depth": 3,
    "length": 644,
    "lastModified": 1568190870541
  },
  {
    "path": "gatling/results/resultId/debugId-request.txt",
    "type": "FILE",
    "depth": 3,
    "length": 24,
    "lastModified": 1568190870501
  }
]
```

```bash
curl http://localhost:8080/files/get/content?path=gatling/results/resultId/debugId-request.txt
```

```
requestBodyFile Content!
```

```bash
curl http://localhost:8080/files/get/content?path=gatling/results/resultId/debugId-response.txt
```

```
responseBodyFile Content!
```

## Delete a Result

```bash
curl -X DELETE http://localhost:8081/result?resultId=resultId
```

```
resultId
```

## Start a Task

```bash
curl -d '{}' -H "ApplicationId: shell" -H "Content-Type: application/json" -X POST http://localhost:8082/task/RUN?description=description
```

```
e4aff6e2-a3ff-483a-9e47-55febe2fd51d
```

### List tasks

```bash
curl http://localhost:8082/task/list | jq '.'
```

```json
[
  {
    "id": "e4aff6e2-a3ff-483a-9e47-55febe2fd51d",
    "startDate": 0,
    "status": "CREATING",
    "type": "DEBUG",
    "containers": [],
    "description": "description"
  }
]
```

Get associated result (resultId == taskId)

```bask
curl http://localhost:8080/files/get/json?path=gatling/results/e4aff6e2-a3ff-483a-9e47-55febe2fd51d/result.json | jq '.'
```

```json
{
  "id": "e4aff6e2-a3ff-483a-9e47-55febe2fd51d",
  "startDate": 1568211558322,
  "endDate": 0,
  "status": "STARTING",
  "description": "description",
  "type": "DEBUG"
}
```

# Java Applications Manual Tests (ran as docker containers in production)

## Synchronizer

Run the storage synchronizer (must have started storage, analysis, runtime backends + [executed a task](#start-a-task))

```bash
export KRAKEN_STORAGE_URL=http://localhost:8080 \
&& export KRAKEN_RUNTIME_URL=http://localhost:8082 \
&& export KRAKEN_DATA=$(pwd)/integration-tests/testDir/ \
&& export KRAKEN_TASK_ID="taskId" \
&& export KRAKEN_CONTAINER_ID="containerId" \
&& export KRAKEN_FILE_DOWNLOADS="README.md<->gatling/README.md" \
&& export KRAKEN_FOLDER_DOWNLOADS="<->gatling/conf" \
&& export KRAKEN_FILE_UPLOADS="run<->gatling" \
&& make serve APP=:application:storage-synchronizer
```