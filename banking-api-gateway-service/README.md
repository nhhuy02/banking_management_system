docker run --name redis-server -p 6379:6379 -d redis
docker exec -it redis-server redis-cli
KEY * (check jwt)