package utility;

import com.datastax.oss.driver.api.core.addresstranslation.AddressTranslator;
import edu.umd.cs.findbugs.annotations.NonNull;
import jnr.ffi.Runtime;

import java.net.InetSocketAddress;

public class NbdAddressTranslator implements AddressTranslator {

    public NbdAddressTranslator() {
        super();
    }

    @NonNull
    @Override
    public InetSocketAddress translate(@NonNull InetSocketAddress inetSocketAddress) {
        String hostAddress = inetSocketAddress.getAddress().getHostAddress();
        String hostName = inetSocketAddress.getHostName();
        return switch (hostAddress) {
            case "172.24.0.2" -> new InetSocketAddress("cassandranode1", 9042);
            case "172.24.0.3" -> new InetSocketAddress("cassandranode2", 9043);
            case "172.24.0.4" -> new InetSocketAddress("cassandranode3", 9044);
            default -> throw new RuntimeException("Address could not be resolved.");
        };
    }

    @Override
    public void close() {

    }
}
