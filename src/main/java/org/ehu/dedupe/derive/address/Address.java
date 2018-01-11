package org.ehu.dedupe.derive.address;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Address {

    private static final List<Pattern> PATTERNS = Stream.of("calle ", "c/ ", "avinguda", "carrer dels", "carrer del", "carrer de la", "carrer de", "carrer", "carretera de", "Avda. de la", "Avda. de", "avda", "")
            .flatMap(street -> Stream.of(",", "").flatMap(addressSeparator -> getCompile(addressSeparator, street)))
            .collect(Collectors.toList());

    private final Range range;
    private final String suffix;
    private final String street;

    public Address(String range, String suffix, String street) {
        this.range = parse(range);
        this.suffix = suffix;
        this.street = street;
    }

    public static Address matcher(String address) {
        return PATTERNS
                .stream()
                .map(pattern -> getMatcher(address, pattern))
                .filter(Matcher::matches)
                .findFirst()
                .map(matcher -> new Address(StringUtils.trim(matcher.group(2)), StringUtils.trim(matcher.group(3)), StringUtils.trim(matcher.group(1))))
                .orElse(new Address(null, address));
    }

    private static Matcher getMatcher(String address, Pattern compile) {
        return compile.matcher(address);
    }

    private static Stream<Pattern> getCompile(String addressSeparator, String street) {
        String single = "\\d+";
        return Stream.of(single + "-" + single, single + "/" + single, single)
                .map(singleNumberPattern -> Pattern.compile("^" + street + "(.*)" + addressSeparator + " +" + String.format("(%s)", singleNumberPattern) + " *(.*)"));
    }

    private Range parse(String range) {
        if (range == null) {
            return null;
        }

        if (NumberUtils.isDigits(range)) {
            return new Range(Integer.parseInt(range));
        }

        if (range.contains("-")) {
            return getRange(range, "-");
        }

        if (range.contains("/")) {
            return getRange(range, "/");
        }

        return null;
    }

    private Range getRange(String range, String separator) {
        String[] split = range.split(separator);
        return new Range(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
    }

    public Address(String suffix, String street) {
        this.range = null;
        this.suffix = suffix;
        this.street = street;
    }

    public Range getRange() {
        return range;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getStreet() {
        return street;
    }


}
