import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.Test;
import persistence.SchemaInitializer;

import java.util.List;

import static org.mockito.Mockito.*;

public class SchemaInitializerTest {

    @Test
    public void shouldInitializeTable_WhenNoTable(){
        Session session =  mock(Session.class);
        NativeQuery nativeQuery = mock(NativeQuery.class);

        SchemaInitializer db = new SchemaInitializer(session);

        when(session.createSQLQuery("Show tables")).thenReturn(nativeQuery);
        when(nativeQuery.getResultList()).thenReturn(null);

        db.initializeSchemasOrDoNothing();

        verify(nativeQuery.getResultList());
        verify(session).createSQLQuery("show tables");

    }
}
