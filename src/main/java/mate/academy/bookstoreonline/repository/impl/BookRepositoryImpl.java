package mate.academy.bookstoreonline.repository.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreonline.model.Book;
import mate.academy.bookstoreonline.repository.BookRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new EntityNotFoundException("Cannot insert book:" + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Book", Book.class).getResultList();
        } catch (Exception e) {
            throw new EntityNotFoundException("Cannot find all books", e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Book.class, id));
        } catch (Exception e) {
            throw new EntityNotFoundException("Cannot find book with id: " + id, e);
        }
    }
}
