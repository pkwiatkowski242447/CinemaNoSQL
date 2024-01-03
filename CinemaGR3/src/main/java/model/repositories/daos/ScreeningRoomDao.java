package model.repositories.daos;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import mapping_layer.model_rows.ScreeningRoomRow;

import java.util.List;
import java.util.UUID;

@Dao
public interface ScreeningRoomDao {

    // Create methods

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ScreeningRoomQueryProvider.class, entityHelpers = {ScreeningRoomRow.class})
    ScreeningRoom create(int screeningRoomFloor, int screeningRoomNumber, int numberOfSeats) throws ScreeningRoomRepositoryCreateException;

    // Read methods

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ScreeningRoomQueryProvider.class, entityHelpers = {ScreeningRoomRow.class})
    ScreeningRoom findByUUID(UUID screeningRoomId) throws ScreeningRoomRepositoryReadException;

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ScreeningRoomQueryProvider.class, entityHelpers = {ScreeningRoomRow.class})
    List<ScreeningRoom> findAll() throws ScreeningRoomRepositoryReadException;

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ScreeningRoomQueryProvider.class, entityHelpers = {ScreeningRoomRow.class})
    List<ScreeningRoom> findAllActive() throws ScreeningRoomRepositoryReadException;

    // Update methods

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ScreeningRoomQueryProvider.class, entityHelpers = {ScreeningRoomRow.class})
    void update(ScreeningRoom screeningRoom) throws ScreeningRoomRepositoryUpdateException;

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ScreeningRoomQueryProvider.class, entityHelpers = {ScreeningRoomRow.class})
    void expire(ScreeningRoom screeningRoom) throws ScreeningRoomRepositoryUpdateException;

    // Delete methods

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ScreeningRoomQueryProvider.class, entityHelpers = {ScreeningRoomRow.class})
    void delete(ScreeningRoom screeningRoom) throws ScreeningRoomRepositoryDeleteException;

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ScreeningRoomQueryProvider.class, entityHelpers = {ScreeningRoomRow.class})
    void deleteByUUID(UUID screeningRoomId) throws ScreeningRoomRepositoryDeleteException;
}
