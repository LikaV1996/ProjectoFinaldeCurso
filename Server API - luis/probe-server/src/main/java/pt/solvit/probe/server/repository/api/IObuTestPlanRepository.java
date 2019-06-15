/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.api;

import java.util.List;
import pt.solvit.probe.server.repository.model.ObuTestPlanDao;

/**
 *
 * @author AnaRita
 */
public interface IObuTestPlanRepository {

    public ObuTestPlanDao findById(long obuId, long testPlanId);

    public List<ObuTestPlanDao> findAll();

    public List<ObuTestPlanDao> findByObuId(long obuId);

    public List<ObuTestPlanDao> findByTestPlanId(long testPlanId);

    public int add(ObuTestPlanDao obuTestPlanDao);

    public int deleteById(long obuId, long testPlanId);

    public int deleteAllByObuId(long obuId);

    public void update(ObuTestPlanDao obuTestPlanDao);
}
