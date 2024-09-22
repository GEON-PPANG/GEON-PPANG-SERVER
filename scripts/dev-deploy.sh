#!/bin/bash
BUILD_PATH=$(ls /home/ubuntu/app/api-1.0.0-SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_PATH)
echo "> build 파일명: $JAR_NAME"

echo "> build 파일 복사"
DEPLOY_PATH=/home/ubuntu/app/nonstop/jar/
cp $BUILD_PATH $DEPLOY_PATH

PROFILE=dev
echo "> PROFILE = $PROFILE 로 설정"

echo "> application.jar 교체"
RUNNING_APPLICATION=$PROFILE-GunbbangServer.jar   # ??? 그냥 이름 임의로 지정해놓는듯
RUNNING_APPLICATION_PATH=$DEPLOY_PATH$RUNNING_APPLICATION

ln -Tfs $DEPLOY_PATH$JAR_NAME $RUNNING_APPLICATION_PATH

echo "> 현재 구동중인 애플리케이션 pid 확인"
RUNNING_PID=$(pgrep -f $RUNNING_APPLICATION)

if [ -z $RUNNING_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $RUNNING_PID"
  kill -15 $RUNNING_PID
  exit=$?
  sleep 10
  if [ $exit -eq 0 ]; then
      echo "kill -15 명령어로 서버가 다운되었습니다. exit status $exit. kill -15로 종료된 시각: $(date) "
  else
      echo "kill -15 명령어로 서버가 다운되지 않았습니다. exit status $exit."
      echo "kill -9 명령어로 서버를 다운시킵니다. kill -9로 종료 시작한 시각: $(date)"
      kill -9 $RUNNING_PID
      sleep 20
  fi
fi

current_datetime=$(TZ=Asia/Seoul date "+%Y년_%m월_%d일_%H시_%M분_%S")
echo "> $PROFILE 배포. 현재 시각: $current_datetime"
nohup java -jar -Duser.timezone=Asia/Seoul -Dspring.profiles.active=$PROFILE $RUNNING_APPLICATION_PATH >> /home/ubuntu/app/nohup/nohup_$current_datetime.out 2>&1 &
