# -*- coding: utf-8 -*-

from elasticsearch import Elasticsearch, NotFoundError
import urllib
import urllib2
import itertools
from xml.etree import ElementTree

def perm (n, seq):
	i = 0
	for p in itertools.product(seq, repeat=n):
		#print("".join(p))
		#print("\n")
		i += 1
	print "Possible Combinations: %d" % i

perm(4, "ABCDEFGHIJKLMNOPQRSTUVWXYZ")


es = Elasticsearch(['http://es.olympus.com:9200'])

url = "http://fuelplanner.com/api/fuelapi.php"
values = {
'QUERY' : 'FUEL',
'EQPT' : 'B752',
'ORIG' : 'EDDH',
'DEST' : 'EDDF',
'USER': 'xfuel@connect-utb.com',
'ACCOUNT' : 'E5A1B183',
'LICENSE' : 'd55134a45df6d8ab37d6186284bfb775'}

data = urllib.urlencode(values)
response = urllib2.urlopen(url, data)
page = response.read()
# print page

tree = ElementTree.fromstring(page)

# print ElementTree.tostring(tree)
doc = {}
for node in tree.iter():

	if len(node.text) > 2:
		#print node.tag
		#print node.text
		doc[node.tag] = node.text

#res = es.index(index='fuelplanner', doc_type="report", body=doc)
print doc


# curl --data "QUERY=FUEL&EQPT=B752&ORIG=KLAX&DEST=KJFK&METAR=YES&USER=xfuel@connect-utb.com&ACCOUNT=E5A1B183&LICENSE=d55134a45df6d8ab37d6186284bfb775"