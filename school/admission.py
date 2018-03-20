# coding=utf-8
import MySQLdb.cursors
import cookielib, urllib2

#获取网页信息
def getHtml(url):
    html = urllib2.urlopen(url).read()
    return html


#保存网页信息
def saveHtml(file_name, file_content):
    with open('./admin_XML/'+file_name+ ".xml", "wb") as f:
        #   写文件用bytes而不是str，所以要转码
        f.write(file_content)


# 打开数据库连接
#con = MySQLdb.connect("localhost","","","iot" )
con = MySQLdb.connect(
        host='localhost',
        user='',
        passwd='',
        db='iot',
        port=3306,
        charset='utf8',
        cursorclass = MySQLdb.cursors.DictCursor
)


#row['name']:学校名称.xml(表示文科一批)   row['name']+'11’（表示理科一批）    row['name']+'22’（表示文科二批）
#1089：学校代码   10001：地区代码(省份)     10035：理科  10034：文科  _10036：一批 _10037:二批
#aurl = "http://gkcx.eol.cn/schoolhtm/scores/provinceScores1089_10001_10035_10037.xml"
#功能：下载各大院校录取批次信息
with con:
    cur = con.cursor()
    cur.execute('select school_id, name, provice_id from school')
    rows = cur.fetchall()
    for row in rows:
        #data = str(row).decode('unicode-escape')
        #print row
        for key, value in row.iteritems():
            print row['name']
            aurl = "http://gkcx.eol.cn/schoolhtm/scores/provinceScores"+str(row['school_id'])+"_"+str(row['provice_id'])+"_10034_10037.xml"
            json1 = getHtml(aurl)
            cj = cookielib.CookieJar()
            opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cj))
            req = opener.open(aurl,timeout=1)
            meta = req.info()
            file_size = int(meta.getheaders("Content-Length")[0])
            print file_size
            #15420：脏数据
            if file_size != 15420 and file_size != 0:
                print file_size
                saveHtml(row['name']+'22', json1)
                print key, value
            else:
                continue



