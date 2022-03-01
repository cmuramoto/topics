package com.nc.topics.quarkus.xa.test;

import com.nc.topics.quarkus.xa.profiles.H2_Postgre_Profile;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(H2_Postgre_Profile.class)
public class H2_Postgre_XATest extends XATestTemplate {

}
