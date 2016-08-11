#!/usr/bin/env bash

for i in {1..5} 
do 
	curl localhost:8080/async &
	curl localhost:8080/async &
	curl localhost:8080/async &
	curl localhost:8080/async &
	curl localhost:8080/async &

done
