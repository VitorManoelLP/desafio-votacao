FROM gradle:jdk17 as build
WORKDIR /app
ENV TZ=America/Sao_Paulo

RUN apt-get update && apt-get install -y tzdata \
    && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone \
    && rm -rf /var/lib/apt/lists/*

COPY build.gradle settings.gradle /app/

RUN gradle build -x test --no-daemon || return 0

COPY . /app

CMD ["gradle", "bootRun"]

