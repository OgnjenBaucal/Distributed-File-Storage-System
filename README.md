# Disto - Distributed Storage System
A peer-to-peer distribution system.
It's a CLI app, for now it only works locally.
You "upload" a file, it gets chunked into 1MB sized chunks and saved. 
A file manifest is made which remembers the order of the chunks so the file 
can be recovered and the user gets the fileID with which they can recover the file.
