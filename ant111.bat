set path1=%cd%
cd ..
set path2=%cd%
del %path2%\安装包2.20\oainfo\oainfo\sql\oa2.sql /f/s/q
mysqldump -uoadev -poadev -h172.16.10.44 oa2_dev --default-character-set=utf8 --opt --extended-insert=false --triggers -R --hex-blob --single-transaction > %path2%\安装包2.20\oainfo\oainfo\sql\oa2.sql
cd %path1%
ant -debug
call %path2%/安装包2.20/weboa2.0--sqlyog.iss
call cmd