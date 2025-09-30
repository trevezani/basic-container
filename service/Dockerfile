FROM amazoncorretto:25.0.0-alpine3.22 AS jre-build
WORKDIR /app

COPY target/*.jar build/app.jar

RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../app.jar)

RUN jdeps --ignore-missing-deps \
    -q \
    --multi-release 25 \
    --print-module-deps \
    --recursive \
    -cp 'build/dependency/BOOT-INF/lib/*' \
    build/app.jar > jre-deps.info

RUN jlink --verbose \
    --compress 2 \
    --strip-java-debug-attributes \
    --no-header-files \
    --no-man-pages \
    --output jre \
    --add-modules jdk.crypto.ec \
    --add-modules $(cat jre-deps.info)

FROM alpine:3.22.1
WORKDIR /deployment

RUN apk add --no-cache curl

COPY --from=jre-build /app/jre jre

COPY --from=jre-build /app/build/dependency/BOOT-INF/lib app/lib
COPY --from=jre-build /app/build/dependency/META-INF app/META-INF
COPY --from=jre-build /app/build/dependency/BOOT-INF/classes app

RUN addgroup -S ctn_app && adduser -S -G ctn_app ctn_app

ENV JAVA_OPTS="-XX:-HeapDumpOnOutOfMemoryError -XX:+ExitOnOutOfMemoryError -XX:MaxRAMPercentage=80.0"
ENV PARAMS=""

COPY entrypoint.sh /entrypoint.sh 

RUN chmod +x /entrypoint.sh

USER ctn_app

ENTRYPOINT ["/entrypoint.sh"]