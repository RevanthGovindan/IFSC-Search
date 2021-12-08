package com.msf.ifsc.jobs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.SQLException;

import com.msf.ifsc.test.TestConfiguration;

public class IFSCUpdate {
	public static void main(String[] args) throws SQLException {
		TestConfiguration.loadConfiguration();
		try {
            URL website = new URL("https://rbidocs.rbi.org.in/rdocs/content/docs/68774.xlsx");
            ReadableByteChannel rbc;
            rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream("/tmp/ifsc.xlsx");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
