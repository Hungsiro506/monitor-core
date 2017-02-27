Service Extra 

uage : 
//Copy jar file and confing file to remote host. 
scp -p 2222 /home/hungdv/Desktop/extra.jar hungvd8@172.31.8.10:/data/user/hungvd8/

export COMMON_CONFIGURATION_FOLDER=/data/user/hungvd8/config
// export environment variable - path to config file

java -jar extra.jar -i /data/user/hungvd8/config.json
// java -jar extra.jar -i pathToRunnerConfig

