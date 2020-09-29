import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by sanco on 29/09/2020.
 * h2jsontest
 */
public class PGJsonType implements UserType {
    private final int CUSTOM_TYPE = Types.OTHER;
    private final static ObjectMapper jsonMapper = new ObjectMapper();


    @Override
    public int[] sqlTypes() {
        return new int[]{CUSTOM_TYPE};
    }

    @Override
    public Class returnedClass() {
        return JsonNode.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x==null? y==null : ((JsonNode)x).equals((JsonNode)y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return ((JsonNode)x).hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        final String cellContent = rs.getString(names[0]);
        if (cellContent == null) {
            return null;
        }
        try {
            return jsonMapper.readTree(cellContent);
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert jsonb to JsonNode: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, CUSTOM_TYPE);
            return;
        }
        try {
            st.setObject(index, jsonMapper.writeValueAsString(value), CUSTOM_TYPE);
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert JsonNode to jsonb: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value==null? null : ((JsonNode)value).deepCopy();
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
