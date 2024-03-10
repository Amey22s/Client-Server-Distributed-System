CLIENT_IMAGE='app-client'
PROJECT_NETWORK='TCP_UDP'
SERVER_IMAGE='tcp-server'
SERVER_CONTAINER='my-tcp-server'
# run the image and open the required ports
echo "----------Running sever app----------"
docker run -d --rm -p 9002:9002/tcp --name $SERVER_CONTAINER --network $PROJECT_NETWORK $SERVER_IMAGE

echo "----------watching logs from server----------"
docker logs $SERVER_CONTAINER -f