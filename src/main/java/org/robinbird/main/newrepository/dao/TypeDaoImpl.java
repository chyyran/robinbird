package org.robinbird.main.newrepository.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import lombok.NonNull;
import org.robinbird.main.newrepository.dao.entity.CompositionTypeEntity;
import org.robinbird.main.newrepository.dao.entity.InstanceEntity;
import org.robinbird.main.newrepository.dao.entity.RelationEntity;
import org.robinbird.main.newrepository.dao.entity.TypeEntity;

public class TypeDaoImpl implements TypeDao {

    private final EntityManagerFactory emf; // later, if wants to support multi-threads, need to create em per thread with this.
    private final EntityManager em; // currently running with embedded mode and open this when app starts and close when app ends.

    private final Query loadTypeEntityWithNameQuery;
    private final Query loadCompositionTypeEntitiesQuery;
    private final Query deleteCompositionTypeEntityQuery;
    private final Query loadInstanceEntitiesQuery;
    private final Query deleteInstanceEntitiesQuery;
    private final Query loadRelationEntitiesQuery;
    private final Query deleteRelationEntitiesQuery;

    public TypeDaoImpl(@NonNull final EntityManagerFactory emf) {
        this.emf = emf;
        this.em = emf.createEntityManager();
        loadTypeEntityWithNameQuery = em.createQuery("select te from TypeEntity te where te.name = :name");
        loadCompositionTypeEntitiesQuery = em.createQuery("select cte from CompositionTypeEntity cte where cte.typeId = :typeId");
        deleteCompositionTypeEntityQuery = em.createQuery("delete from CompositionTypeEntity cte where cte.typeId = :typeId");
        loadInstanceEntitiesQuery = em.createQuery("select ie from InstanceEntity ie where ie.parentTypeId = :parentTypeId");
        deleteInstanceEntitiesQuery = em.createQuery("delete from InstanceEntity ie where ie.parentTypeId = :parentTypeId");
        loadRelationEntitiesQuery = em.createQuery("select re from RelationEntity re where re.parentTypeId = :parentTypeId");
        deleteRelationEntitiesQuery = em.createQuery("delete from RelationEntity re where re.parentTypeId = :parentTypeId");
    }

    private <T> T transactional(@NonNull final T entity, @NonNull final Consumer<T> consumer) {
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            consumer.accept(entity);
            em.flush();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
        return entity;
    }

    private void transactional(@NonNull final Query query) {
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            query.executeUpdate();
            em.flush();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    private <T> List<T> loadEntities(@NonNull final Query query, @NonNull final String paramName, final long id) {
        List result = query.setParameter(paramName, id)
                           .getResultList();
        List<T> results = new ArrayList<>();
        Iterator iter = result.iterator();
        while (iter.hasNext()) {
            results.add((T)iter.next());
        }
        return results;
    }

    @Override
    public Optional<TypeEntity> loadTypeEntity(final long id) {
        TypeEntity entity = em.find(TypeEntity.class, id);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<TypeEntity> loadTypeEntity(@NonNull final String name) {
        List result = loadTypeEntityWithNameQuery.setParameter("name", name)
                                                 .getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of((TypeEntity)result.get(0));
    }

    @Override
    public TypeEntity saveTypeEntity(@NonNull final TypeEntity te) {
        return transactional(te, em::persist);
    }

    @Override
    public void removeTypeEntity(@NonNull final TypeEntity te) {
        transactional(te, em::remove);
    }

    @Override
    public List<CompositionTypeEntity> loadCompositionTypeEntities(final long typeId) {
        return loadEntities(loadCompositionTypeEntitiesQuery, "typeId", typeId);
    }

    @Override
    public CompositionTypeEntity saveCompositionTypeEntity(@NonNull final CompositionTypeEntity compositionTypeEntity) {
        return transactional(compositionTypeEntity, em::persist);
    }

    @Override
    public void removeCompositionTypeEntities(final long typeId) {
        transactional(deleteCompositionTypeEntityQuery.setParameter("typeId", typeId));
    }

    @Override
    public List<InstanceEntity> loadInstanceEntities(final long parentTypeId) {
        return loadEntities(loadInstanceEntitiesQuery, "parentTypeId", parentTypeId);
    }

    @Override
    public InstanceEntity saveInstanceEntity(@NonNull final InstanceEntity instanceEntity) {
        return transactional(instanceEntity, em::persist);
    }

    @Override
    public void removeInstanceEntity(InstanceEntity instanceEntity) {
        transactional(instanceEntity, em::remove);
    }

    @Override
    public void removeInstanceEntities(long parentTypeId) {
        transactional(deleteInstanceEntitiesQuery.setParameter("parentTypeId", parentTypeId));
    }

    @Override
    public List<RelationEntity> loadRelationEntities(long parentTypeId) {
        return loadEntities(loadRelationEntitiesQuery, "parentTypeId", parentTypeId);
    }

    @Override
    public RelationEntity saveRelationEntity(RelationEntity relationEntity) {
        return transactional(relationEntity, em::persist);

    }

    @Override
    public void removeRelationEntity(RelationEntity relationEntity) {
        transactional(relationEntity, em::remove);
    }

    @Override
    public void removeRelationEntities(long parentTypeId) {
        transactional(deleteRelationEntitiesQuery.setParameter("parentTypeId", parentTypeId));
    }

}
