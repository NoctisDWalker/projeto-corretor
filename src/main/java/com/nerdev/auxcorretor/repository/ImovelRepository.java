package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.Imovel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.UUID;

public interface ImovelRepository extends JpaRepository<Imovel, UUID>, JpaSpecificationExecutor<Imovel> {

    List<Imovel> findByCorretorResponsavelId(UUID corretorResponsavelId);

    @Modifying
    @Query("UPDATE Imovel i SET i.quantidadeVisitas = i.quantidadeVisitas + 1 WHERE i.id = :imovelId")
    void imcrementarVisitas(UUID imovelid);

    @Modifying
    @Query("UPDATE Imovel i SET i.quantidadeVisitas = i.quantidadeVisitas - 1 WHERE i.id = :imovelId")
    void decrementarVisitas(UUID imovelid);

    @Query("UPDATE Imovel i SET i.quantidadeVisitas = (SELECT COUNT(v) FROM Visita v WHERE v.imovel.id = i.id AND v.statusVisita <> 'CANCELADA')")
    @Modifying
    void recalcularTodasQuantidadeVisitas();

    @Override
    @EntityGraph(attributePaths = {"corretorResponsavel"})
    Page<Imovel> findAll(Specification<Imovel> spec, Pageable pageable);

}
