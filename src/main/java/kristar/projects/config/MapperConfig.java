package kristar.projects.config;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.data.repository.config.ImplementationDetectionConfiguration;

@org.mapstruct.MapperConfig(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl"
)
public class MapperConfig {
}
