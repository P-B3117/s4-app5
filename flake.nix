{
  description = "Run AnalLex Java app";

  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";

  outputs = { self, nixpkgs }: {
    packages.x86_64-linux.default = let
      pkgs = import nixpkgs { system = "x86_64-linux"; };
    in pkgs.stdenv.mkDerivation {
      name = "anallex";
      src = self;

      buildInputs = [ pkgs.jdk21 ];

      buildPhase = ''
        mkdir -p bin
        javac -d bin $(find . -name "*.java")
        jar cvfm AnalLex.jar manifest.txt -C bin src
      '';

      installPhase = ''
        mkdir -p $out/bin
        cp AnalLex.jar $out/
        cat > $out/bin/anallex <<EOF
        #!${pkgs.runtimeShell}
        exec ${pkgs.jdk21}/bin/java -jar $out/AnalLex.jar "\$@"
        EOF
        chmod +x $out/bin/anallex
      '';
    };

    packages.aarch64-darwin.default = let
      pkgs = import nixpkgs { system = "aarch64-darwin"; };
    in pkgs.stdenv.mkDerivation {
      name = "anallex";
      src = self;

      buildInputs = [ pkgs.jdk21 ];

      buildPhase = ''
        mkdir -p bin
        javac -d bin $(find . -name "*.java")
        jar cvfm AnalLex.jar manifest.txt -C bin src
      '';

      installPhase = ''
        mkdir -p $out/bin
        cp AnalLex.jar $out/
        cat > $out/bin/anallex <<EOF
        #!${pkgs.runtimeShell}
        exec ${pkgs.jdk21}/bin/java -jar $out/AnalLex.jar "\$@"
        EOF
        chmod +x $out/bin/anallex
      '';
    };

    apps.x86_64-linux.default = {
      type = "app";
      program = "${self.packages.x86_64-linux.default}/bin/anallex";
    };

    apps.aarch64-darwin.default = {
      type = "app";
      program = "${self.packages.aarch64-darwin.default}/bin/anallex";
    };
  };
}
