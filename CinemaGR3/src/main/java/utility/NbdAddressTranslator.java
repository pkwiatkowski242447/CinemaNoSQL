package utility;

import com.datastax.oss.driver.api.core.addresstranslation.AddressTranslator;
import com.datastax.oss.driver.api.core.context.DriverContext;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.net.InetSocketAddress;

public class NbdAddressTranslator implements AddressTranslator {

    public NbdAddressTranslator(DriverContext driverContext) {

    }

    @NonNull
    @Override
    public InetSocketAddress translate(InetSocketAddress address) {
        String hostAddress = address.getAddress().getHostAddress();
        String hostName = address.getHostName();
        return switch (hostAddress) {
            case "172.24.0.2" -> new InetSocketAddress("cassandranode1", 9042);
            case "172.24.0.3" -> new InetSocketAddress("cassandranode2", 9043);
            case "172.24.0.4" -> new InetSocketAddress("cassandranode3", 9044);
            default -> throw new RuntimeException("Given address could not be translated correctly.");
        };
    }

    @Override
    public void close() {

    }
}
