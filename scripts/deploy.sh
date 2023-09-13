#!/bin/bash
BUILD_PATH=$(ls /home/ubuntu/app/api-0.0.1-SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_PATH)
echo "> build 파일명: $JAR_NAME"

echo "> build 파일 복사"
DEPLOY_PATH=/home/ubuntu/app/nonstop/jar/
cp $BUILD_PATH $DEPLOY_PATH

echo "> 현재 구동중인 Set 확인"
CURRENT_PROFILE=$(curl -s https://www.gunbbang.org/profile)
echo "> $CURRENT_PROFILE"

# 쉬고 있는 set 찾기: set1이 사용중이면 set2가 쉬고 있고, 반대면 set1이 쉬고 있음
if [ $CURRENT_PROFILE == dev-set1 ]
then
  IDLE_PROFILE=dev-set2
  IDLE_PORT=8082
elif [ $CURRENT_PROFILE == dev-set2 ]
then
  IDLE_PROFILE=dev-set1
  IDLE_PORT=8081
else
  echo "> 일치하는 Profile이 없습니다. Profile: $CURRENT_PROFILE"
  echo "> dev-set1을 할당합니다. IDLE_PROFILE: dev-set1"
  IDLE_PROFILE=dev-set1
  IDLE_PORT=8081
fi

echo "> application.jar 교체"
IDLE_APPLICATION=$IDLE_PROFILE-GunbbangServer.jar   # ??? 그냥 이름 임의로 지정해놓는듯
IDLE_APPLICATION_PATH=$DEPLOY_PATH$IDLE_APPLICATION

ln -Tfs $DEPLOY_PATH$JAR_NAME $IDLE_APPLICATION_PATH

echo "> $IDLE_PROFILE 에서 구동중인 애플리케이션 pid 확인"
IDLE_PID=$(pgrep -f $IDLE_APPLICATION)

if [ -z $IDLE_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $IDLE_PID"
  kill -15 $IDLE_PID
  exit=$?
  sleep 60
  if [ exit -eq 0 ]; then
      echo "kill -15 명령어로 서버가 다운되었습니다. exit status $exit. kill -15로 종료된 시각: $(date) "
  else
      echo "kill -15 명령어로 서버가 다운되지 않았습니다. exit status $exit."
      echo "kill -9 명령어로 서버를 다운시킵니다. kill -9로 종료 시작한 시각: $(date)"
      kill -9 $IDLE_PID
  fi
  sleep 30
fi

echo "> $IDLE_PROFILE 배포"
nohup java -jar -Duser.timezone=Asia/Seoul -Dspring.profiles.active=$IDLE_PROFILE $IDLE_APPLICATION_PATH >> /home/ubuntu/app/nohup.out 2>&1 &

current_datetime=$(date "+%Y년 %m월 %d일 %H시 %M분 %S초")
echo "배포된 시간: $current_datetime"
echo "> $IDLE_PROFILE 10초 후 Health check 시작"
echo "> curl -s http://localhost:$IDLE_PORT/actuator/health "
sleep 10

# health check부분
for retry_count in {1..10}
do
  response=$(curl -s http://localhost:$IDLE_PORT/actuator/health)
  up_count=$(echo $response | grep 'UP' | wc -l)

  if [ $up_count -ge 1 ]
  then # $up_count >= 1 ("UP" 문자열이 있는지 검증)
      echo "> Health check 성공"
      break
  else
      echo "> Health check의 응답을 알 수 없거나 혹은 status가 UP이 아닙니다."
      echo "> Health check: ${response}"
  fi

  if [ $retry_count -eq 10 ]
  then
    echo "> Health check 실패. "
    echo "> Nginx에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  echo "> Health check 연결 실패. 재시도..."
  sleep 10
done

echo "> 스위칭"
sleep 10
/home/ubuntu/app/nonstop/switch.sh >> /home/ubuntu/app/nonstop/switch_log.out 2>&1 &
