package org.acme;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.acme.model.MovieModel;

import java.lang.String;
import java.lang.Integer;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class MoviesResource {
	public static List<MovieModel> movies = new ArrayList<>();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("movie")
	public Response getMovies() {
		return Response.ok(movies).build();
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("movie/size")
	public Integer countMovies() {
		return movies.size();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("movie")
	public Response createMovie(MovieModel newMovie) {
		movies.add(newMovie);
		return Response.ok(movies).build();
	}
	
	@PUT
	@Path("movie/{id}/{title}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMovie(
		@PathParam("id") Long id,
		@PathParam("title") String title) 
	{
		movies = movies.stream().map(movie -> {
			if(movie.getId().equals(id)) {
				movie.setTitle(title);
			}
			return movie;
		}).collect(Collectors.toList());
		
		return Response.ok(movies).build();
	}
	
	
	@DELETE
	@Path("movie/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteMovie(@PathParam("{id}") Long id) {
		
		Optional<MovieModel> movieToDelete = movies.stream().filter(movie -> movie.getId().equals(id)).findFirst();
		
		boolean removed = false;
		
		if (movieToDelete.isPresent()) {
			removed = movies.remove(movieToDelete.get());
		}
		
		if (removed) {
			return Response.noContent().build();
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
		
		//return removed ? Response.noContent().build() : Response.status(Response.Status.BAD_REQUEST).build();
	}	
}