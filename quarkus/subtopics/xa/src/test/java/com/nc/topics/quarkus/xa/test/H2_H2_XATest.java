package com.nc.topics.quarkus.xa.test;

import com.nc.topics.quarkus.xa.profiles.H2_H2_Profile;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(H2_H2_Profile.class)
public class H2_H2_XATest extends XATestTemplate {

}
