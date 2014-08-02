#!/usr/bin/env python2.7

import urllib
import json
import subprocess
import os

host = "http://localhost:5984/literable/{}"

views = urllib.urlopen(host.format("_all_docs?limit=50&start_key=%22_design/%22&end_key=%22_design/\uefff%22")).read()

# first delete existing design docs
for row in json.loads(views)['rows']:
	rev = row['value']['rev']
	doc = row['id']

	subprocess.Popen(['curl', '-X', 'DELETE', host.format(doc + "?rev={}".format(rev))]).communicate()


view_dir = os.path.join(os.path.dirname(os.path.realpath(__file__)), "views")
# now create new ones
for view_def in os.listdir(view_dir):
	ddoc_name = view_def.split('.')[0]
	print host.format('_design/{} -d @{}/{}'.format(ddoc_name, view_dir, view_def))
	subprocess.Popen(['curl', '-X', 'PUT', host.format('_design/{}'.format(ddoc_name)), '-d', '@{}/{}'.format(view_dir, view_def)]).communicate()