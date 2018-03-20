# coding=utf-8
import MySQLdb.cursors
import cookielib, urllib2


def getHtml(url):
    html = urllib2.urlopen(url).read()
    return html


def saveHtml(file_name, file_content):
    with open('./school_XML/'+file_name+ ".xml", "wb") as f:
        #   写文件用bytes而不是str，所以要转码  
        f.write(file_content)

# 打开数据库连接
con = MySQLdb.connect(
    host='localhost',
    user='',
    passwd='',
    db='iot',
    port=3306,
    charset='utf8',
    cursorclass=MySQLdb.cursors.DictCursor
    )

#下载各学校专业录取分数线
with con:
    cur = con.cursor()
    cur.execute('select school_id, name, provice_id from school')
    rows = cur.fetchall()
    for row in rows:
        #data = str(row).decode('unicode-escape')
        #print row
        for key, value in row.iteritems():
            print row['name']
            aurl = "http://gkcx.eol.cn/commonXML/schoolSpecialPoint/schoolSpecialPoint"+str(row['school_id'])+"_"+str(row['provice_id'])+"_10034.xml"
            json1 = getHtml(aurl)
            cj = cookielib.CookieJar()
            opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cj))
            req = opener.open(aurl,timeout=10)
            meta = req.info()
            file_size = int(meta.getheaders("Content-Length")[0])
            #print file_size
            #15420：脏数据
            if file_size != 15420 and file_size != 0:
                print file_size
                saveHtml(row['name']+'1', json1)
                #print key, value
            else:
                break

# qjnu 曲靖师范学院http://gkcx.eol.cn/commonXML/schoolSpecialPoint/schoolSpecialPoint1089_10001_10035.xml
#1089：学校代码   10001：地区代码     10035：理科  10034：文科
