package cloud.grup.cloudgrupRESTAPI.services;

import cloud.grup.cloudgrupRESTAPI.commands.EntryForm;
import cloud.grup.cloudgrupRESTAPI.domain.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntryService {
    Entry getById(Long id);
    void delete(Long id);
    Entry save(Entry entry);
    Entry saveEntryForm(EntryForm entryform);
    List<Entry> listAll();
}
