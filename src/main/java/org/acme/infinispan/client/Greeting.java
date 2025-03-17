package org.acme.infinispan.client;

import java.io.Serializable;

public record Greeting(String name, String message) implements Serializable {
}
