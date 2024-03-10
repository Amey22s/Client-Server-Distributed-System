PROJECT_NETWORK='TCP_UDP'
SERVER_IMAGE='udp-server'
SERVER_CONTAINER='my-udp-server'
# run the image and open the required ports
echo "----------Running sever app----------"
docker run --rm -d -p 9001:9001/udp  --name $SERVER_CONTAINER --network $PROJECT_NETWORK $SERVER_IMAGE

echo "----------watching logs from server----------"
docker logs $SERVER_CONTAINER -f