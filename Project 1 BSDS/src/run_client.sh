CLIENT_IMAGE='app-client'
PROJECT_NETWORK='TCP_UDP'
TCP_SERVER_CONTAINER='my-tcp-server'
UDP_SERVER_CONTAINER='my-udp-server'
TCP_CLIENT_CONTAINER='my_tcp_client'
UDP_CLIENT_CONTAINER='my_udp_client'

if [ $# -ne 1 ]
then
  echo "Usage: ./run_client.sh <protocol>"
  exit
fi

if [ "$1" == "tcp" ]
then
  docker run -it --rm --name $TCP_CLIENT_CONTAINER\
  --network $PROJECT_NETWORK $CLIENT_IMAGE \
  java Client.UnifiedClient $TCP_SERVER_CONTAINER "9002" "$1"
fi

if [ "$1" == "udp" ]
then
  docker run -it --rm --name $UDP_CLIENT_CONTAINER\
  --network $PROJECT_NETWORK $CLIENT_IMAGE \
  java Client.UnifiedClient $UDP_SERVER_CONTAINER "9001" "$1"
fi