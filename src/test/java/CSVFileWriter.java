import downloads.BinanceData;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import persistence.DataWriter;

import java.io.IOException;
import java.util.LinkedList;

import static org.mockito.Mockito.*;

public class CSVFileWriter {

    BinanceData data;

    @Rule
    public TemporaryFolder tempF = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        tempF.newFile("test.csv");
        data = mock(BinanceData.class);
        when(data.getOpenTime()).thenReturn(new LinkedList<Long>());

    }

    @Test
    public void FileWriterWriteData_Using_getOpenTime(){
        DataWriter dw = new persistence.CSVFileWriter();
        dw.writeData(data);
        verify(data).getOpenTime();
    }
}
