# SmartHome Memory for ROSjava
This package is a ROSJava Memory for ROS SmartHome.

For :
* Android platform
* ROSJava node platform

Test Query :

SHOW MEASUREMENTS;
SELECT * FROM node ORDER BY time DESC LIMIT 3;
SELECT * FROM node_comm ORDER BY time DESC LIMIT 3;
SELECT * FROM node_electrical ORDER BY time DESC LIMIT 3;
SELECT * FROM node_gaz ORDER BY time DESC LIMIT 3;
SELECT * FROM node_luminous ORDER BY time DESC LIMIT 3;
SELECT * FROM node_thermal ORDER BY time DESC LIMIT 3;
SELECT * FROM node_open ORDER BY time DESC LIMIT 3;
SELECT * FROM node_media ORDER BY time DESC LIMIT 3;

SHOW MEASUREMENTS;
SELECT * FROM node_electrical ORDER BY time DESC LIMIT 3;
SELECT * FROM node_gas ORDER BY time DESC LIMIT 3;
SELECT * FROM node_luminous ORDER BY time DESC LIMIT 3;
SELECT * FROM node_thermal ORDER BY time DESC LIMIT 3;
SELECT * FROM user_activity ORDER BY time DESC LIMIT 3;
