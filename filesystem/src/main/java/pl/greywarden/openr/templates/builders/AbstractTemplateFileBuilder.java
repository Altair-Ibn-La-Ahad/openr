package pl.greywarden.openr.templates.builders;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractTemplateFileBuilder {

    protected final String targetPath;

    public abstract void build();

}
