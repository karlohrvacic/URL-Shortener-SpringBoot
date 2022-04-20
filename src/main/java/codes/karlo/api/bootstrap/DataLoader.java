package codes.karlo.api.bootstrap;

import codes.karlo.api.repository.AuthoritiesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final AuthoritiesRepository authoritiesRepository;

    public DataLoader(final AuthoritiesRepository authoritiesRepository) {
        this.authoritiesRepository = authoritiesRepository;
    }

    @Override
    public void run(final String... args) throws Exception {


    }
}
