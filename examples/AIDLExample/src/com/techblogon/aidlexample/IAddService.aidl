package com.techblogon.aidlexample;


// Declare the communication interface which holds all of our exposed functions.
interface IAddService {
    // WE can pass values ALONG WITH in, out, or inout parameters. 
    // Android Java Primitive datatypes (such as int,, string, boolean, etc.) can only be passed in.
    int add(in int ValueFirst, in int valueSecond);
}