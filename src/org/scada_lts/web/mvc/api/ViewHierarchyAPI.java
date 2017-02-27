/*
 * (c) 2017 Abil'I.T. http://abilit.eu/
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package org.scada_lts.web.mvc.api;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scada_lts.dao.model.viewshierarchy.ViewHierarchyNode;
import org.scada_lts.service.ViewHierarchyService;
import org.scada_lts.service.model.ViewHierarchyJSON;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serotonin.mango.Common;
import com.serotonin.mango.vo.User;

/**
 * Controller for API View Hierarchy
 * 
 * @author Grzesiek Bylica grzegorz.bylica@gmail.com
 */
@Controller 
public class ViewHierarchyAPI {
	
	private static final Log LOG = LogFactory.getLog(ViewHierarchyAPI.class);
	
	@Resource
	private ViewHierarchyService viewHierarchyService;
	
	@RequestMapping(value = "/api/view_hierarchy/getAll", method = RequestMethod.GET)
	public ResponseEntity<String> getAll(HttpServletRequest request) {
		LOG.info("/api/view_hierarchy/getAll");
		try {
			User user = Common.getUser(request);
		
			if (user != null) {
				
				List<ViewHierarchyJSON> data = viewHierarchyService.getAll();
				String json = null;
				ObjectMapper mapper = new ObjectMapper();
				
				json = mapper.writeValueAsString(data);
				
				return new ResponseEntity<String>(json,HttpStatus.OK);				
			} 
			
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			
		} catch (Exception e) {
			LOG.error(e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	
		
	
	@RequestMapping(value = "/api/view_hierarchy/createFolder/{name}/{parentId}", method = RequestMethod.GET)
	public ResponseEntity<String> createFolder(@PathVariable("name") String name, @PathVariable("parentId") int parentId, HttpServletRequest request) {
		
		LOG.info("/api/view_hierarchy/createFolder/{name}:"+name+" {parentId}:"+parentId);
		
		try {
			User user = Common.getUser(request);
			if (user != null) {
				ViewHierarchyNode node = new ViewHierarchyNode(parentId, name);
				if (viewHierarchyService.add(node)) {
					String json = null;
					ObjectMapper mapper = new ObjectMapper();
					json = mapper.writeValueAsString(node);
					return new ResponseEntity<String>(json,HttpStatus.OK);
				}
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
			
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			
		} catch (Exception e) {
			LOG.error(e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/api/view_hierarchy/editFolder/{name}/{key}", method = RequestMethod.GET)
	public ResponseEntity<String> editFolder(@PathVariable("name") String name, @PathVariable("key") int key, HttpServletRequest request) {
		
		LOG.info("/api/view_hierarchy/editFolder/{name}:"+name+" {key}:"+key);
		
		try {
			User user = Common.getUser(request);
			if (user != null) {
				
				ViewHierarchyNode node = new ViewHierarchyNode(ViewHierarchyService.ROOT_ID, name);
				node.setId(key);
				
				if (viewHierarchyService.edt(node)) {
					String json = null;
					ObjectMapper mapper = new ObjectMapper();
					json = mapper.writeValueAsString(node);
					return new ResponseEntity<String>(json,HttpStatus.OK);
				}
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
			
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			
		} catch (Exception e) {
			LOG.error(e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/api/view_hierarchy/deleteFolder/{id}", method = RequestMethod.GET)
	public ResponseEntity<String> getChartData(@PathVariable("id") int id, HttpServletRequest request) {
		
		LOG.info("/api/watchlist/deleteFolder/{id}:"+id);
		
		try {
			User user = Common.getUser(request);
			if (user != null) {
				
				if (viewHierarchyService.del(id)) {
					String json = null;
					ObjectMapper mapper = new ObjectMapper();
					json = mapper.writeValueAsString("dell");
					return new ResponseEntity<String>(json,HttpStatus.OK);
				}
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
			
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		
		} catch (Exception e) {
			LOG.error(e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@RequestMapping(value = "/api/view_hierarchy/move/{id}/{newParentId}", method = RequestMethod.GET)
	public ResponseEntity<String> move(@PathVariable("newParentId") int id, @PathVariable("newParentId") int newParentId, HttpServletRequest request) {
		
		LOG.info("/api/view_hierarchy/move/{id}/{newParentId} id:"+id+" newParentId:"+newParentId);
		
		try {
			User user = Common.getUser(request);
			if (user != null) {
				
				if (viewHierarchyService.move(id, newParentId)) {
					String json = null;
					ObjectMapper mapper = new ObjectMapper();
					json = mapper.writeValueAsString("move");
					return new ResponseEntity<String>(json,HttpStatus.OK);
				}
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
			
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		
		} catch (Exception e) {
			LOG.error(e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@RequestMapping(value = "/api/view_hierarchy/getFirstViewId", method = RequestMethod.GET)
	public ResponseEntity<String> getFirstViewId(HttpServletRequest request) {
		
		LOG.info("/api/view_hierarchy/getFirstViewId");
		
		try {
			User user = Common.getUser(request);
			if (user != null) {
				String json = null;
				ObjectMapper mapper = new ObjectMapper();
				json = mapper.writeValueAsString(viewHierarchyService.getFirstViewId());
				return new ResponseEntity<String>(json,HttpStatus.OK);
			}
			
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		
		} catch (Exception e) {
			LOG.error(e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@RequestMapping(value = "/api/view_hierarchy/checkNameFolder/{name}", method = RequestMethod.GET)
	public ResponseEntity<String> checkNameFolder(@PathVariable("name") String name, HttpServletRequest request) {
		
		LOG.info("/api/view_hierarchy/checkNameFolder: ");
		
		try {
			User user = Common.getUser(request);
			if (user != null) {
				String json = null;
				ObjectMapper mapper = new ObjectMapper();
				json = mapper.writeValueAsString(viewHierarchyService.isUsedName(name));
				return new ResponseEntity<String>(json,HttpStatus.OK);
			}
			
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		
		} catch (Exception e) {
			LOG.error(e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		
	}

}
