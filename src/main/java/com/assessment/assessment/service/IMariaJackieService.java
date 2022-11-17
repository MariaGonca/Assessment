package com.assessment.assessment.service;

import com.assessment.assessment.model.MariaJackie;
import java.util.List;

public interface IMariaJackieService {

    public List<MariaJackie> findAll();

    public MariaJackie findById(Long id);

    public MariaJackie save(MariaJackie mj);

    public void delete(Long id);

}
