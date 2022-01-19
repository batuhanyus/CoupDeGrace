package com.derebeylik.coupdegrace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by user on 2/12/2017.
 */

public class HelpRequest implements Serializable
{
    public String cityName;
    public String requester;
    public String coordinates;
    public String info;
    public List<String> helpers;
    //public List<String> seenBy;

    public HelpRequest(){}

    public HelpRequest(String _cityName, String _requester, String _coordinates, String _info, String _myself)
    {
        cityName=_cityName;
        requester=_requester;
        coordinates=_coordinates;
        info=_info;
        helpers=new ArrayList<String>();
        helpers.add(_myself);
    }

    @Override
    public String toString()
    {
        return cityName + requester + coordinates + info;
    }
}
