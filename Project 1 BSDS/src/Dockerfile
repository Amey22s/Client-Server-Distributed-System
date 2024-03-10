FROM bellsoft/liberica-openjdk-alpine-musl:11 AS client-build
COPY . src/
WORKDIR /src
RUN javac Client/UnifiedClient.java

FROM bellsoft/liberica-openjdk-alpine-musl:11 AS server-udp-build
COPY . src/
WORKDIR /src
RUN javac Server/UDPDriver.java
CMD ["java", "Server/UDPDriver", "9001"]

FROM bellsoft/liberica-openjdk-alpine-musl:11 AS server-tcp-build
COPY . src/
WORKDIR /src
RUN javac Server/TCPDriver.java
CMD ["java", "Server/TCPDriver", "9002"]