package nl.rug.search.cpptool.api.util;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.List;

import static nl.rug.search.cpptool.api.util.ContextTools.pathSplitter;
import static org.junit.Assert.assertEquals;

public class ContextToolsTest {

    static <T> List<T> listOf(T... elems) {
        return ImmutableList.copyOf(elems);
    }

    @Test
    public void testPathSplitter() {
        assertEquals(
                listOf("gbxutilacfr", "SubStatus", "fault(const class std::__1::basic_string<char> &)"),
                pathSplitter("::gbxutilacfr::SubStatus::fault(const class std::__1::basic_string<char> &)")
        );
        assertEquals(
                listOf("gbxutilacfr", "SubStatus"),
                pathSplitter("::gbxutilacfr::SubStatus")
        );
        assertEquals(
                listOf("gbxutilacfr", "SubStatus", "~SubStatus()"),
                pathSplitter("::gbxutilacfr::SubStatus::~SubStatus()")
        );
        assertEquals(listOf(), pathSplitter(""));
        assertEquals(listOf(), pathSplitter("::"));
    }
}
