package com.resumebuilder.jdanalyzer.model;

import java.util.List;

public record SynonymMatch(
        String jdTerm,
        String canonicalTerm,
        boolean presentInResume
) {
}
