/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.impl.util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author AnaRita
 */
public class ServiceUtil {

    public static <R, T> List<R> map(List<T> serviceList, Function<T, R> mapper) {
        return serviceList
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
}
