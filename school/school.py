# coding=utf-8
import urllib2
import json

def getHtml(url):
    html = urllib2.urlopen(url).read()
    return html


def saveHtml(file_name, file_content):
    with open('./school_JSON/'+file_name.replace('/', '_') + ".json", "wb") as f:
        #   写文件用bytes而不是str，所以要转码
        f.write(file_content)


#def provinceJson():
with open("./province.json", 'r') as load_f:
    load_dict = json.load(load_f)
    print(load_dict)

dict = load_dict
for key, value in dict.iteritems():
    json1 = getHtml(value)
    saveHtml(key,json1)

#print dict.keys()
#print dict.values()

