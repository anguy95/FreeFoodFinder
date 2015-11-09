# FreeFoodFinder
CSE110 project to find free food around you. 


#### BUG/TODO

* If you time the CEA and the update 15sec, crashes hard
Since update happens every 15, map bubbles get thrown out
-> fixed i think (randy, blame me if it's not)

* ListView resets when fetching data (go back to the top)
-- Most likely listview doesn't auto update (update on tab switch + user requests) 

* Parse auto-delete
